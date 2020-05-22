import api from '../../api/index.js';
import { myInformationTemplate } from '../../utils/templates.js';

function MyPage() {
  const $myInformationContainer = document.querySelector("#my-information-container");

  const initMyPageView = () => {
    api.me.find()
    .then(response => {
      if (response.status === 200) {
        return response.json();
      }
      throw new Error("사용자 초기값을 찾는데 실패했습니다!")
    }).then(meResponse => {
      $myInformationContainer.innerHTML = myInformationTemplate(meResponse);
    }).catch(error => {
      alert(error.message);
      return location.href="/login";
    });
  }

  this.init = () => {
    initMyPageView();
  }
}

const myPage = new MyPage()
myPage.init()