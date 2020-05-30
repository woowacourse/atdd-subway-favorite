import {EVENT_TYPE} from "../../utils/constants.js";

function Index() {
  const $login = document.querySelector("#login");
  const $logout = document.querySelector("#logout");
  const $join = document.querySelector("#join");
  const $myPage = document.querySelector("#my-page");
  const $myPageEdit = document.querySelector("#my-page-edit");
  const $myFavorites = document.querySelector("#my-favorites");

  this.init = () => {
    $logout.addEventListener(EVENT_TYPE.CLICK, logout);
    if (localStorage.getItem("token")) {
      toggleElements();
    }
  }

  function logout() {
    localStorage.removeItem("token");
    alert("로그아웃 하였습니다.");
    toggleElements();
  }

  function toggleElements() {
    $login.classList.toggle("hidden");
    $logout.classList.toggle("hidden");
    $join.classList.toggle("hidden");
    $myPage.classList.toggle("hidden");
    $myPageEdit.classList.toggle("hidden");
    $myFavorites.classList.toggle("hidden");
  }
}

const index = new Index()
index.init()
