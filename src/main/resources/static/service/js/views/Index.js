import {EVENT_TYPE} from "../../utils/constants.js";

function Index() {
    const $optionsContainer = document.querySelector(".options-container");

    const onLogoutHandler = (event) => {
        event.preventDefault();
        localStorage.setItem("jwt", "");
        location.href = "/";
    }

    this.init = () => {
        const jwt = localStorage.getItem("jwt");
        if (jwt !== "") {
            const loginTemplate = `<li class="inline-block p-1">
                <a id="index-logout" href="/logout" class="underline">로그아웃</a>
                </li>`
            $optionsContainer.insertAdjacentHTML("afterbegin", loginTemplate);
            document.querySelector("#index-logout").addEventListener(EVENT_TYPE.CLICK, onLogoutHandler);
        } else {
            const notLoginTemplate = `<li class="inline-block p-1">
                <a href="/login" class="underline">로그인</a>
                </li>
                <li class="inline-block p-1">
                <a href="/join" class="underline">회원가입</a>
                </li>`
            $optionsContainer.insertAdjacentHTML("afterbegin", notLoginTemplate);
        }
    }
}


const index = new Index();
index.init();