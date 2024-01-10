// 검색 로직
let selectedStockName = ""; // 선택된 주식명 저장

if (document.getElementById('searchButtonSelect')) {
    document.getElementById('searchButtonSelect').addEventListener('click', function(event) {
        event.preventDefault();
        selectedStockName = document.getElementById('stockNameSelect').value;

        fetch(`http://localhost:8000/api/stock?name=${selectedStockName}`)
            .then(response => response.json())
            .then(data => {
                updateTable(data);
            })
            .catch(error => console.error('Error:', error));
    });
}

// 검색 데이터 테이블 업데이트 함수
function updateTable(stock) {
    var tableBody = document.querySelector('table tbody');
    tableBody.innerHTML = '';
    var row = `
        <tr>
            <td class="stock-name" data-name="${stock.Name}">${stock.Name}</td>
            <td>${stock.Close}</td>
            <td>${stock.Open}</td>
            <td>${stock.High}</td>
            <td>${stock.Low}</td>
            <td>${stock.Volume}</td>
            <td>${stock.Changes}</td>
        </tr>
    `;
    tableBody.innerHTML = row;
}

// 페이지 버튼 !!!
// 전역 변수로 현재 페이지 번호 및 페이지 범위 관리
let currentPage = 0;
const itemsPerPage = 20;
let totalNumberOfPages;
let currentPageRange = 1; 
const rangeSize = 10

// 총 페이지 수 계산 및 페이지네이션 초기화 함수
function initializePagination() {
    totalNumberOfPages = 48;
    createPagination();
}

// 페이지 버튼 생성 함수
function createPagination() {
    const paginationContainer = document.getElementById('pagination');
    const startPage = (currentPageRange - 1) * rangeSize;
    const endPage = Math.min(startPage + rangeSize, totalNumberOfPages);

    paginationContainer.innerHTML = ''; // 기존 내용 초기화

    // 이전 페이지 범위 버튼
    if (currentPageRange > 1) {
        const prevRangeButton = document.createElement('button');
        prevRangeButton.innerText = '이전 범위';
        prevRangeButton.addEventListener('click', function() {
            currentPageRange--;
            createPagination();
            fetchPageData(startPage - rangeSize);
        });
        paginationContainer.appendChild(prevRangeButton);
    }

    // 개별 페이지 버튼
    for (let i = startPage; i < endPage; i++) {
        const pageButton = document.createElement('button');
        pageButton.innerText = i + 1;
        pageButton.classList.add('page-button');
        pageButton.addEventListener('click', function() {
            currentPage = i;
            fetchPageData(i);
        });
        paginationContainer.appendChild(pageButton);
    }

    // 다음 페이지 범위 버튼
    if (endPage < totalNumberOfPages) {
        const nextRangeButton = document.createElement('button');
        nextRangeButton.innerText = '다음 범위';
        nextRangeButton.addEventListener('click', function() {
            currentPageRange++;
            createPagination();
            fetchPageData(endPage);
        });
        paginationContainer.appendChild(nextRangeButton);
    }

    // 첫 페이지 데이터 로드
    fetchPageData(startPage);
}

// 페이지 데이터 가져오기 함수
function fetchPageData(pageNumber) {
    fetch(`http://localhost:8000/api/stocks?page=${pageNumber}&limit=${itemsPerPage}`)
        .then(response => response.json())
        .then(data => updateTableWithStocks(data))
        .catch(error => console.error('Error:', error));
}

// 페이지 로드 시 페이지네이션 초기화
document.addEventListener('DOMContentLoaded', function() {
    if (document.body.classList.contains('main-page')) {
        initializePagination();
        fetchPageData(0);
        startAutoRefresh(); 
    }
});

// 정렬기능
let currentSortColumn = null;
let currentSortDirection = 'asc'; // 'asc' or 'desc'

function sortTable(columnName) {
    // 서버에 정렬 요청
    console.log('Sorting by: ' + columnName);
    const sortDirection = (columnName === currentSortColumn && currentSortDirection === 'asc') ? 'desc' : 'asc';
    fetch(`http://localhost:8000/api/stocks?sort=${columnName}&direction=${sortDirection}`)
        .then(response => response.json())
        .then(data => {
            currentSortColumn = columnName;
            currentSortDirection = sortDirection;
            updateTableWithStocks(data);
        })
        .catch(error => console.error('Error:', error));
}

// 페이지 넘길 때 가져오는 데이터
function updateTableWithStocks(data, clearTable = true) {
    const tableBody = document.querySelector('table tbody');
    if (clearTable) tableBody.innerHTML = ''; // 테이블 초기화

    data.forEach(stock => {
        const row = document.createElement('tr'); // 새로운 행 생성
        row.innerHTML = `
            <td>${stock.Name}</td>
            <td>${stock.Close}</td>
            <td>${stock.Open}</td>
            <td>${stock.High}</td>
            <td>${stock.Low}</td>
            <td>${stock.Volume}</td>
            <td>${stock.Changes}</td>
        `;

        row.addEventListener('click', () => {
            window.location.href = `charts?name=${stock.Name}`; // 행 클릭 시 이동
        });

        tableBody.appendChild(row); // 생성된 행을 테이블에 추가
    });
}

    // 페이지네이션 초기화
    createPagination();
    

// 10초마다 페이지 데이터를 가져오는 함수를 시작하는 함수
function startAutoRefresh() {
    setInterval(() => {
        fetchPageData(currentPage); // 현재 페이지 번호를 사용하여 데이터 가져오기
    }, 10000); // 10000ms = 10초
}

// 날짜별 데이터 가져오기 함수 (select.html용)
function fetchStocksByDate(period) {
    if (!selectedStockName) {
        alert('먼저 주식을 검색해 주세요.');
        return;
    }

    fetch(`http://localhost:8000/api/stockData?selectedStockName=${selectedStockName}&period=${period}`)
        .then(response => response.json())
        .then(data => {
            drawChart(data); // 차트 그리기
        })
        .catch(error => console.error('Error:', error));
}

let chartInstance = null; // 차트 인스턴스를 저장하기 위한 전역 변수

function createChart(ctx, labels, dataPoints, backgroundColors) {
    return new Chart(ctx, {
        type: 'line', // 차트의 종류 (예: line, bar, pie 등)
        data: {
            labels: labels,
            datasets: [{
                label: '주식 가격',
                data: dataPoints,
                backgroundColor: backgroundColors,
                borderColor: 'rgba(0, 123, 255, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: false
                }
            }
        }
    });
}

// 차트 그리기 함수
function drawChart(data) {
    const ctx = document.getElementById('stockChart').getContext('2d');

    // 기존 차트가 존재한다면 파괴
    if (chartInstance) {
        chartInstance.destroy();
    }

    const labels = data.map(item => item['Date']); // 날짜 데이터
    const dataPoints = data.map(item => item['Close']); // 종가 데이터

    const backgroundColors = dataPoints.map((_, index) => {
        if (index >= dataPoints.length - 10) {
            return 'rgba(255, 0, 0, 0.5)'; // 빨간색
        } else {
            return 'rgba(0, 123, 255, 0.5)'; // 파란색
        }
    });
    // 새 차트 생성
    chartInstance = createChart(ctx, labels, dataPoints, backgroundColors);
}
