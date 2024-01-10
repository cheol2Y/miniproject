// 등락률 데이터 출력 로직
document.addEventListener("DOMContentLoaded", async function () {
  try {
    const response = await fetch("http://localhost:8000/api/changes");
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();

    // Process the data and update the HTML table
    const tableBody = document.getElementById("mytable");

    data.forEach((stock) => {
      // Create a new row for each stock
      const row = document.createElement("tr");

      row.innerHTML = `
                <td>${stock.Code}</td>
                <td>${stock.Name}</td>
                <td>${stock.Close}</td>
                <td>${stock.Changes}</td>
            `;
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Error:", error);
  }
});

document.addEventListener("DOMContentLoaded", async function () {
  try {
    const response = await fetch("http://localhost:8000/api/changesDown");
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();

    // Process the data and update the HTML table
    const tableBody = document.getElementById("mytable2");

    data.forEach((stock) => {
      // Create a new row for each stock
      const row = document.createElement("tr");
      row.innerHTML = `
                <td>${stock.Code}</td>
                <td>${stock.Name}</td>
                <td>${stock.Close}</td>
                <td>${stock.Changes}</td>
            `;
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Error:", error);
  }
});

document.addEventListener("DOMContentLoaded", async function () {
  try {
    const response = await fetch("http://localhost:8000/api/kospi");
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const stockData = await response.json(); // API 응답에서 데이터를 가져옴
    const chartData = stockData.map((item) => ({
      x: item.Date,
      y: item.Close,
    }));

    const ctx = document.getElementById("myChart2").getContext("2d");
    const myChart2 = new Chart(ctx, {
      type: "line",
      data: {
        datasets: [
          {
            label: "주식 가격",
            data: chartData, // 가져온 데이터를 사용
            backgroundColor: "rgba(255, 99, 132, 0.2)",
            borderColor: "rgba(255, 99, 132, 1)",
            borderWidth: 2,
            pointRadius: 3,
            pointBackgroundColor: "rgba(255, 99, 132, 1)",
            fill: true,
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: false,
          },
        },
        plugins: {
          tooltip: {
            callbacks: {
              label: (context) =>
                new Intl.NumberFormat("en-US", {
                  style: "currency",
                  currency: "KRW",
                }).format(context.parsed.y),
            },
          },
        },
        elements: {
          line: {
            tension: 0.2,
          },
        },
      },
    });
  } catch (error) {
    console.error("Error:", error);
  }
});

document.addEventListener("DOMContentLoaded", async function () {
  try {
    const response = await fetch("http://localhost:8000/api/kospi_sub");
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();

    // Process the data and update the HTML span element
    const kospiCloseElement = document.getElementById("kospiClose");

    // Assuming data is an array and you want to show the 'Close' value of the first item
    if (data.length > 0) {
      kospiCloseElement.textContent = data[data.length -1].Close;
    } else {
      console.error("No data available.");
    }
  } catch (error) {
    console.error("Error:", error);
  }
});

document.addEventListener("DOMContentLoaded", async function () {
  try {
    const response = await fetch("http://localhost:8000/api/kospi_sub");
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();

    // Process the data and update the HTML span element
    const kospiCloseElement = document.getElementById("kospiOpen");

    // Assuming data is an array and you want to show the 'Close' value of the first item
    if (data.length > 0) {
      kospiCloseElement.textContent = data[data.length -1].Open;
    } else {
      console.error("No data available.");
    }
  } catch (error) {
    console.error("Error:", error);
  }
});

document.addEventListener("DOMContentLoaded", async function () {
  try {
    const response = await fetch("http://localhost:8000/api/kospi_sub");
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();

    // Process the data and update the HTML span element
    const kospiCloseElement = document.getElementById("kospiChange");

    // Assuming data is an array and you want to show the 'Close' value of the first item
    if (data.length > 0) {
      const ratio = data[data.length -1].Close - data[data.length -2].Close;
      const roundedRatio = Math.round(ratio * 100) / 100;
      kospiCloseElement.textContent = roundedRatio.toString();
    } else {
      console.error("No data available.");
    }
  } catch (error) {
    console.error("Error:", error);
  }
});

document.addEventListener("DOMContentLoaded", async function () {
  try {
    const response = await fetch("http://localhost:8000/api/kospi_sub");
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();

    // Process the data and update the HTML span element
    const kospiCloseElement = document.getElementById("CloseUpDown");

    // Assuming data is an array and you want to show the 'Close' value of the first item
    if (data.length > 0) {
      const ratio = data[data.length -1].Close / data[data.length -2].Close;
      const roundedRatio = Math.round(ratio * 10000) / 10000; // Round to 4 decimal places
      kospiCloseElement.textContent = roundedRatio.toString() + "%";
    } else {
      console.error("No data available.");
    }
  } catch (error) {
    console.error("Error:", error);
  }
});

document.addEventListener("DOMContentLoaded", async function () {
  try {
    const response = await fetch("http://localhost:8000/api/kospi_sub");
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();

    // Process the data and update the HTML span element
    const kospiOpenElement = document.getElementById("OpenUpDown");

    // Assuming data is an array and you want to show the 'Close' value of the first item
    if (data.length > 0) {
      const ratio = data[data.length -1].Open / data[data.length -2].Open;
      const roundedRatio = Math.round(ratio * 10000) / 10000; // Round to 4 decimal places
      kospiOpenElement.textContent = roundedRatio.toString() + "%";
    } else {
      console.error("No data available.");
    }
  } catch (error) {
    console.error("Error:", error);
  }
});
