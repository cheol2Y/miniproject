import pandas as pd
import FinanceDataReader as fdr
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from typing import Optional
from datetime import datetime, timedelta
import pymysql
import holidays

# StockDataApp 클래스 정의
# 오늘의 날짜를 'YYYY-MM-DD' 형식으로 반환하는 함수


def get_today_date_formatted():

    today = datetime.today()
    formatted_date = today.strftime('%Y-%m-%d')
    return formatted_date

# 오늘 날짜에서 7일 전의 날짜를 'YYYY-MM-DD' 형식으로 반환하는 함수


def get_one_week_ago_date_formatted():

    today = datetime.today()
    one_week_ago = today - timedelta(days=30)
    formatted_date = one_week_ago.strftime('%Y-%m-%d')
    return formatted_date


class StockDataApp:
    def __init__(self):
        self.kospi_df = None
        self.conn = None
        self.result_list = []
        self.kr_holidays = holidays.SouthKorea()

    def load_data(self):
        show_cell = ['Code', 'Name', 'Close', 'Changes', 'ChagesRatio',
                     'Open', 'High', 'Low', 'Volume', 'Amount', 'Stocks']
        self.kospi_df = fdr.StockListing('KOSPI')[show_cell]

    def get_kospi_df(self):
        show_cell = ['Code', 'Name', 'Close', 'Changes', 'ChagesRatio',
                     'Open', 'High', 'Low', 'Volume', 'Amount', 'Stocks']
        return fdr.StockListing('KOSPI')[show_cell]

# 변화율 상위 5개 데이터 함수

    def load_chage_data(self):
        kospi_df = self.get_kospi_df()
        show_cell = ['Code', 'Name', 'Close', 'Changes']
        data = kospi_df[show_cell]
        data['Close'] = pd.to_numeric(
            data['Close'], errors='coerce').astype('Int64')
        data['Changes'] = round(data['Changes'] / data['Close'] * 100, 2)

        data = data.sort_values(by='Changes', ascending=False).head(5)
        return data.to_dict(orient='records')

# 변화율 하위 5개 데이터 함수
    def load_chage_data2(self):
        kospi_df = self.get_kospi_df()
        show_cell = ['Code', 'Name', 'Close', 'Changes']
        data = kospi_df[show_cell]
        data['Close'] = pd.to_numeric(
            data['Close'], errors='coerce').astype('Int64')
        data['Changes'] = round(data['Changes'] / data['Close'] * 100, 2)

        data = data.sort_values(by='Changes', ascending=True).head(5)
        return data.to_dict(orient='records')


# 대쉬보드 메인 차트 데이터


    def main_chart_data(self):

        startDate = get_one_week_ago_date_formatted()
        endDate = get_today_date_formatted()
        data = fdr.DataReader('KS11', startDate, endDate)
        data.reset_index(inplace=True)
        data = data[['Date', 'Close']]
        data['Date'] = data['Date'].dt.strftime('%Y-%m-%d')
        return data.to_dict(orient='records')

    def main_sub_data(self):
        startDate = get_one_week_ago_date_formatted()
        endDate = get_today_date_formatted()
        data = fdr.DataReader('KS11', startDate, endDate)
        data.reset_index(inplace=True)
        data = data[['Open', 'Close', 'Change']]
        data['Change'] = round(data['Change'], 5)
        return data.to_dict(orient='records')

    def get_stock(self, name):
        search = self.kospi_df[self.kospi_df['Name'] == name]
        if search.empty:
            raise HTTPException(status_code=404, detail="Stock not found")
        stock = search.iloc[0]
        open_price = float(stock['Open'])
        close_price = float(stock['Close'])
        change = (close_price - open_price) / open_price * 100
        stock_dict = stock.to_dict()
        stock_dict['Changes'] = round(change, 2)  # 소수점 둘째자리까지 반올림
        return stock_dict

    def connect_db(self):
        self.conn = pymysql.connect(
            host='localhost',
            user='urstory',
            password='u1234',
            db='miniprojectdb',
            charset='utf8'
        )

    def load_predict_data(self):
        cursor = self.conn.cursor(pymysql.cursors.DictCursor)
        sql = '''
            SELECT p.*
            FROM predict p
            INNER JOIN (
                SELECT MAX(date) AS max_date
                FROM predict
            ) md ON p.date = md.max_date;
            '''
        cursor.execute(sql)
        predictlist = cursor.fetchall()
        for stock in predictlist:
            company_name = stock['name']
            if not self.result_list or self.result_list[-1][0]['Name'] != company_name:
                self.result_list.append([])
            last_date = None
            for i in range(1, 11):
                if last_date is None:
                    current_date = self.adjust_date(datetime.now(), i)
                else:
                    current_date = self.adjust_date(last_date, 1)
                date_value = current_date.strftime("%Y-%m-%d")
                close_value = stock[f"day{i}"]
                entry = {'Name': company_name,
                         'Date': date_value, 'Close': close_value}
                self.result_list[-1].append(entry)
                last_date = current_date

    def adjust_date(self, current_date, delta_days):
        adjusted_date = current_date + timedelta(days=delta_days)
        while adjusted_date.weekday() >= 5 or adjusted_date in self.kr_holidays:
            adjusted_date += timedelta(days=1)
        return adjusted_date

    def item_code_by_stock_name(self, item_name):
        item_code_list = self.kospi_df.loc[self.kospi_df["Name"]
                                           == item_name, "Code"].tolist()
        if len(item_code_list) > 0:
            return item_code_list[0]
        else:
            return None

    def get_stock_data(self, selectedStockName, period):
        item_code = self.item_code_by_stock_name(selectedStockName)
        if not item_code:
            raise HTTPException(status_code=404, detail="Stock not found")

        end_date = datetime.today()
        start_date = self.adjust_date(
            end_date, -{'1week': 7, '1month': 30, '3months': 90, '6months': 180, '1year': 365}.get(period, 0))

        if start_date != end_date:
            formatted_start_date = start_date.strftime('%Y-%m-%d')
            formatted_end_date = end_date.strftime('%Y-%m-%d')
            stock_data = fdr.DataReader(
                item_code, start=formatted_start_date, end=formatted_end_date)
            stock_data_list = [{
                'Name': selectedStockName,
                'Date': idx.strftime('%Y-%m-%d'),
                'Open': row['Open'],
                'High': row['High'],
                'Low': row['Low'],
                'Close': row['Close'],
                'Volume': row['Volume'],
                'Change': row['Change']
            } for idx, row in stock_data.iterrows()]

            result_data = next(
                (data for data in self.result_list if data[0]['Name'] == selectedStockName), None)
            return stock_data_list + result_data if result_data else stock_data_list
        else:
            raise HTTPException(status_code=400, detail="Invalid period")

    def get_all_stocks(self, page=0, limit=15):
        kospi_df = self.get_kospi_df()
        start = page * limit
        end = start + limit
        stocks = kospi_df.iloc[start:end]

        for idx, stock in stocks.iterrows():
            open_price = float(stock['Open'])
            close_price = float(stock['Close'])
            change = (close_price - open_price) / open_price * 100
            stocks.at[idx, 'Changes'] = str(round(change, 2)) + "%"

        return stocks.to_dict(orient='records')


# FastAPI 애플리케이션 및 설정
stock_app = StockDataApp()

app = FastAPI()

origins = ["http://localhost",
          "http://localhost:8080", "http://localhost:8000"]
app.add_middleware(CORSMiddleware, allow_origins=origins,
                  allow_credentials=True, allow_methods=["*"], allow_headers=["*"])


@app.on_event("startup")
async def startup_event():
    stock_app.load_data()
    stock_app.connect_db()
    stock_app.load_predict_data()


# 변화율 상위 5개
@app.get("/api/changes")
async def get_changes_data():
    return stock_app.load_chage_data()

# 변화율 하위 5개


@app.get("/api/changesDown")
async def get_changes2_data():
    return stock_app.load_chage_data2()


@app.get("/api/kospi")
async def get_kospi_data():
    return stock_app.main_chart_data()


@app.get("/api/kospi_sub")
async def get_kospi_sub_data():
    return stock_app.main_sub_data()


@app.get("/api/stock")
async def get_stock(name: str):
    return stock_app.get_stock(name)


@app.get("/api/stocks")
async def get_all_stocks(page: Optional[int] = 0, limit: Optional[int] = 15):
    return stock_app.get_all_stocks(page, limit)


@app.get("/api/stockData")
async def get_stock_data(selectedStockName: str, period: str):
    return stock_app.get_stock_data(selectedStockName, period)