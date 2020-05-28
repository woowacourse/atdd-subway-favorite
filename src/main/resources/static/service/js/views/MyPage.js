import api from '../../api/index.js';

function MyPage() {
    const $emailValue = document.querySelector('#email');
    const $nameValue = document.querySelector('#name');

    const onLoad = () => {
        const accessToken = localStorage.getItem("accessToken");
        const tokenType = localStorage.getItem("tokenType");
        const headers = tokenType + " " + accessToken;
        api.member
            .find(headers)
            .then(data => {
                $emailValue.textContent = data.email;
                $nameValue.textContent = data.name;
            })
    };

    this.init = () => {
        onLoad();
    }
}

const myPage = new MyPage();
myPage.init();
