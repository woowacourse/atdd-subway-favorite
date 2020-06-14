import api from "../../api/index.js";

function MyPage() {
    const $userEmail = document.querySelector("#mypage-email");
    const $userName = document.querySelector("#mypage-name");

    const initUserInfo = async () => {
        const token = sessionStorage.getItem("accessToken");
        await api.login.getUserInfo(token).then(data => {
            $userEmail.innerText = data.email;
            $userName.innerText = data.name;
        })
    }

    this.init = () => {
        initUserInfo();
    }
}

const myPage = new MyPage()
myPage.init()