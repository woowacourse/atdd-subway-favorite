import {EVENT_TYPE, HTTP_HEADERS} from '../../utils/constants.js'
import api from '../../api/index.js'

function MyPageEdit() {
    const $updateButton = document.querySelector('#update-button')
    const $email = document.querySelector('#email')
    const $name = document.querySelector('#name')
    const $password = document.querySelector('#password')
    const $passwordCheck = document.querySelector('#password-check')
    let memberId
    let token

    const show = async () => {
        token = window.sessionStorage.getItem(HTTP_HEADERS.AUTHORIZATION)
        const response = await api.member.getWithAuth(token)
        memberId = response.id
        $email.value = response.email
        $name.value = response.name
    }
    const onUpdate = event => {
        event.preventDefault()
        const nameInput = $name.value
        const passwordInput = $password.value
        const passwordCheckInput = $passwordCheck.value

        if (!nameInput || !passwordInput || passwordInput !== passwordCheckInput) {
            alert("NONONONO")
        }

        const updateDetail = {
            name: nameInput,
            password: passwordInput
        }

        api.member.update(memberId, token, updateDetail)
    }

    this.init = () => {
        show()
        $updateButton.addEventListener(EVENT_TYPE.CLICK, onUpdate)
    }
}

const myPageEdit = new MyPageEdit()
myPageEdit.init()
