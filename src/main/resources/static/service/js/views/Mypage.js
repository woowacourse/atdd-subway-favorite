import api from '../../api/index.js'

function Mypage() {
  const $memberEmailValue = document.querySelector('#member-email')
  const $memberNameValue = document.querySelector('#member-name')

  const initMyPage = () => {
    api.member.find()
      .then(data => {
        $memberEmailValue.innerHTML = data.email
        $memberNameValue.innerHTML = data.name
      })
  }

  this.init = () => {
    initMyPage()
  }
}

const mypage = new Mypage()
mypage.init()
