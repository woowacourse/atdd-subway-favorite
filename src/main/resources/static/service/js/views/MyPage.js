import api from '../../api/index.js'

function MyPage() {
  this.init = () => {
    const email = document.querySelector('#email');
    const name = document.querySelector('#name');

    api.member.get().then(res => {
      if (!res.ok) {
        throw res;
      }
      res.json().then(member => {
        email.innerText = member.email;
        name.innerText = member.name;
      });
    }).catch(error => {
      error.json().then(error => alert(error.message))
      window.location.href = '/login';
    })
  }
}

const myPage = new MyPage();
myPage.init()
