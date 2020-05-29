import {HTTP_HEADERS} from '../../utils/constants.js'
import api from '../../api/index.js'

function MyPage() {
  const $editButton = document.querySelector('#my-page-edit-button')
  const $email = document.querySelector('#my-page-email')
  const $name = document.querySelector('#my-page-name')

  const show = async () => {
    const token = window.sessionStorage.getItem(HTTP_HEADERS.AUTHORIZATION)
    const response = await api.member.getWithAuth(token)

    $email.innerText = response.email
    $name.innerText = response.name
  }
  const onEdit = event => {
  }

  this.init = () => {
    show()
    // $editButton.addEventListener(EVENT_TYPE.CLICK, onEdit)
  }
}

const myPage = new MyPage()
myPage.init()
