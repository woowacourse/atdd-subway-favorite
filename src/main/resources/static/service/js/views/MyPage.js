function MyPage() {
    const $emailValue = document.querySelector('#email-value');
    const $nameValue = document.querySelector('#name-value');

    function requestMyPageData(email, token) {
        fetch("/members?email=" + email, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': "Bearer " + token
            }
        })
            .then(data => data.json())
            .then(data => {
                    $emailValue.innerHTML = data.email;
                    $nameValue.innerHTML = data.name;
                }
            )
    }

    this.loadMyPage = () => {
        const token = sessionStorage.getItem("token");
        if (token === null) {
            $emailValue.value = "로그인하고 돌아오세요!";
            $nameValue.value = "로그인하고 돌아오세요!";
            alert("로그인되지 않았습니다. 로그인하고 돌아오세요~");
            return;
        }
        const email = sessionStorage.getItem("email");

        requestMyPageData(email, token);
    }
}

const myPage = new MyPage();
myPage.loadMyPage();