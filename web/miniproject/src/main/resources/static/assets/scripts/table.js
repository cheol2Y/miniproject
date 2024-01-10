const changeColumns = document.querySelectorAll('.col-7[data-label="change"]');

// 각 열에 대해 처리
changeColumns.forEach((column) => {
  const changeValue = column.textContent.trim();

  // 등락율이 +인 경우 초록색, -인 경우 빨간색으로 글자색 설정
  if (changeValue.includes("+")) {
    column.style.color = "green";
  } else if (changeValue.includes("-")) {
    column.style.color = "red";
  }
});
