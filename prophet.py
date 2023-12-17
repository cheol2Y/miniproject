#!/usr/bin/env python
# coding: utf-8

# # 모듈 임포트 및 데이터 로드

# In[1]:


from tqdm.auto import tqdm
import random
import os

import torch

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# In[ ]:


def reset_seeds(seed=42):
    random.seed(seed)
    os.environ['PYTHONHASHSEED'] = str(seed)
    np.random.seed(seed)
    torch.manual_seed(seed)
    torch.cuda.manual_seed(seed)
    torch.backends.cudnn.deterministic = True

reset_seeds()

# In[ ]:


device = 'cuda' if torch.cuda.is_available() else 'cpu'
device

# In[ ]:


# In[ ]:


import FinanceDataReader as fdr

# In[ ]:


df_krx = fdr.StockListing('KRX')
df_krx.head()

# In[ ]:


df_kospi = df_krx[df_krx['Market'] == 'KOSPI']
df_kospi

# # 종목명을 받아 종목코드를 찾아 반환하는 함수

# In[ ]:


def item_code_by_item_name(item_name):
    """
    종목명을 받아 종목코드를 찾아 반환하는 함수
    """
    item_code_list = df_kospi.loc[df_kospi["Name"] == item_name, "Code"].tolist()
    if len(item_code_list) > 0:
        item_code = item_code_list[0]
        return item_code
    else:
        return "없는 주식입니다 !"

# # 종목명을 넘겨주면 일별시세를 반환하는 함수

# In[ ]:


def find_item_list(item_name, year=2020):
    """
    종목명을 넘겨주면 일별시세를 반환하는 함수
    내부에서 종목명으로 종목코드를 반환하는 함수(item_code_by_item_name)로
    종목의 시세를 수집합니다.
    """

    item_code = item_code_by_item_name(item_name)
    if item_code:
        df_day = fdr.DataReader(item_code, str(year))
        return df_day
    else:
        return False

# In[ ]:


df = find_item_list("삼성전자", year = 2020)
df

# # 시각화

# In[ ]:


data = df['Close'].values

plt.figure(figsize=(12, 8))
plt.plot(data)
plt.show()

# # 스케일링 및 사이즈 변경
# 

# In[ ]:


from sklearn.preprocessing import MinMaxScaler
import numpy as np

data = np.array(data).reshape(-1,1)    # 표준화를 하기 위해 사이즈를 (-1,1)로 조정

scaler = MinMaxScaler(feature_range=(0, 1))
scaled = scaler.fit_transform(data)
scaled[1]

# In[ ]:


test_idx = int(len(scaled) * 0.8)

train = scaled[:test_idx]
test = scaled[test_idx:]

# In[ ]:


df['ds'] = pd.to_datetime(df.index)
df['y'] = df['Close']

# In[ ]:


df[['ds', 'y']].iloc[:-10]

# # 모델 세팅 및 학습, 예측

# In[ ]:




# prophet 모듈 세팅
from prophet import Prophet

# Day 단위로 데이터가 구성되어 있으므로, 일 단위 주기성 활성화
model = Prophet(daily_seasonality=True)

# 데이터 학습 시작 -> 기계학습
model.fit(df[['ds', 'y']].iloc[:-10])

# 주가 예측 위한 날짜 데이터 세팅 -> 기존 데이터 + 향후 14일치 예측값
future = model.make_future_dataframe(periods=10)

# 주가 예측
forecast = model.predict(future)

forecast[['ds', 'yhat', 'yhat_lower', 'yhat_upper']].tail(11)

# 모델이 제공하는 시각화
model.plot(forecast)

# In[ ]:


forecast[['ds', 'yhat', 'yhat_lower', 'yhat_upper']]

# In[ ]:


model = Prophet(seasonality_mode = 'multiplicative',
                yearly_seasonality=True,
                weekly_seasonality=True,
                daily_seasonality=True,
                changepoint_prior_scale=0.5)

# 데이터 학습 시작 -> 기계학습
model.fit(df[['ds', 'y']])

# 주가 예측 위한 날짜 데이터 세팅 -> 기존 데이터 + 향후 10일치 예측값
future = model.make_future_dataframe(periods=14)

forecast = model.predict(future)

target_changes = forecast['yhat'].tail(14)

forecast[['ds', 'yhat', 'yhat_lower', 'yhat_upper']]

# In[ ]:


model.plot(forecast)

# In[ ]:


future_pred = forecast[['ds', 'yhat', 'yhat_lower', 'yhat_upper']].tail(14)
