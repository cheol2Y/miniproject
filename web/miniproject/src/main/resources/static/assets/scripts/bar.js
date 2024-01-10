document.addEventListener("DOMContentLoaded", function() {
    fetch('/api/user/email')
        .then(response => response.text())
        .then(email => {
            const userImage = document.getElementById('userImg');
            const userEmailSpan = document.getElementById('userEmail');
            const loginLink = document.getElementById('loginLink');
            const logoutLink = document.getElementById('logoutLink');

            if(email) {
                userEmailSpan.textContent = email;
                userImage.src = 'assets/img/user.png'; // 사용자 이미지 경로
                logoutLink.style.display = 'block';
                loginLink.style.display = 'none';
            } else {
                userEmailSpan.textContent = '로그인 필요';
                userImage.src = 'assets/img/visitor.png'; // 방문자 이미지 경로
                logoutLink.style.display = 'none';
                loginLink.style.display = 'block';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('userImg').src = 'assets/img/visitor.png'; // 기본 이미지 경로
            document.getElementById('userEmail').textContent = '이메일 정보 가져오기 실패';
            document.getElementById('loginLink').style.display = 'block';
            document.getElementById('logoutLink').style.display = 'none';
        });
});

document.addEventListener('DOMContentLoaded', function() {
    fetch('/fetch-message')
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('No message available');
            }
        })
        .then(data => {
            if (data && data.message) {
                alert(data.message); // 메시지를 alert로 표시
            }
        })
        .catch(error => console.error(error.message));
});