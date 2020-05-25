import api from "../../api/index.js";

function MyPage() {
  const $name = document.querySelector("#member-name");
  const $email = document.querySelector("#member-email");

  this.init = async () => {
    const memberDetails = await api.member.get(localStorage.getItem("token"));

    $name.textContent = memberDetails.name;
    $email.textContent = memberDetails.email;
  }
}

const myPage = new MyPage()
myPage.init().then();
