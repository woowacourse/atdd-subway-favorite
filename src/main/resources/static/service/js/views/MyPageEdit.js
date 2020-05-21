import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Edit() {
    const $emailInput = document.querySelector('#email')
    const $nameInput = document.querySelector('#name')
    const $passwordInput = document.querySelector('#password')
    const $passwordCheckInput = document.querySelector('#password-check')
    const $updateButton = document.querySelector('#update-button')
    const $deleteButton = document.querySelector('#delete-button')

    const onUpdate = async event => {
        event.preventDefault()

        const nameValue = $nameInput.value
        const passwordValue = $passwordInput.value
        const passwordCheckValue = $passwordCheckInput.value

        if (!nameValue && !passwordValue && !passwordCheckValue) {
            Snackbar.show({text: ERROR_MESSAGE.UPDATE_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
            return
        }

        const updateData = {
            name: nameValue,
            password: passwordValue,
            confirmPassword: passwordCheckValue
        }

        try {
            await api.me.update(updateData);
            Snackbar.show({text: "회원 정보가 수정되었습니다 👍🏻", pos: 'bottom-center', showAction: false, duration: 2000})
            setTimeout(() => {
                location.href = '/mypage'
            }, 1000)
        } catch (e) {
            Snackbar.show({text: e, pos: 'bottom-center', showAction: false, duration: 2000})
        }
    }

    const onDelete = async event => {

    }

    this.init = async () => {
        try {
            const myInfo = await api.me.retrieve();
            $emailInput.value = myInfo.email
            $nameInput.value = myInfo.name
        } catch (e) {
            Snackbar.show({text: e, pos: 'bottom-center', showAction: false, duration: 2000})
        }
        $updateButton.addEventListener(EVENT_TYPE.CLICK, onUpdate)
        $deleteButton.addEventListener(EVENT_TYPE.CLICK, onDelete)
    }
}

const edit = new Edit()
edit.init()
