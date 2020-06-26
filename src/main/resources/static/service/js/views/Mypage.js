import api from '../../api/index.js'

function Mypage() {
    const $memberEmailValue = document.querySelector('#member-email')
    const $memberNameValue = document.querySelector('#member-name')

    const initMyPage = () => {
        const id = localStorage.getItem("memberId")
        api.member.find(id)
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
