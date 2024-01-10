function updateDateTime() {
  var currentDate = new Date();
  var year = currentDate.getFullYear();
  var month = ("0" + (currentDate.getMonth() + 1)).slice(-2);
  var day = ("0" + currentDate.getDate()).slice(-2);
  var hours = ("0" + currentDate.getHours()).slice(-2);
  var minutes = ("0" + currentDate.getMinutes()).slice(-2);
  var seconds = ("0" + currentDate.getSeconds()).slice(-2);

  var formattedDate =
    year +
    "-" +
    month +
    "-" +
    day +
    " " +
    hours +
    ":" +
    minutes +
    ":" +
    seconds;

  document.getElementById("currentDateTime").innerHTML = formattedDate;
}

// 페이지 로드 후 초기 업데이트
updateDateTime();

// 1초마다 업데이트
setInterval(updateDateTime, 1000);
