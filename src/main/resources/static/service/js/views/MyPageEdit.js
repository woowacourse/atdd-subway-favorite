import api from '../../api/index.js'
import showSnackbar from "../../lib/snackbar/index.js";

function MypageEdit() {
    const $email = document.querySelector('#email')
    const $name = document.querySelector('#name')
    const $password = document.querySelector('#password')
    const $passwordCheck = document.querySelector('#password-check')
    const $editButton = document.querySelector("#edit-button")
    const $resignButton = document.querySelector("#resign-button")

    const onResignHandler = (event) => {
        try {
            event.preventDefault()

            const data = {
                id: event.target.dataset.id,
            }

            api.member.delete(data)
                .then(() => {
                    localStorage.clear()
                    location.href = '/'
                })

        } catch (e) {
            showSnackbar(e.message)
        }
    }

    const validate = () => {
        if ($password.value !== $passwordCheck.value) {
            throw Error("비밀번호와 비밀번호 확인이 다릅니다.")
        }
    }

    const onEditHandler = (event) => {
        try {
            event.preventDefault()

            validate()

            const data = {
                id: event.target.dataset.id,
                email: $email.value,
                name: $name.value,
                password: $password.value,
            }

            api.member.edit(data)
                .then(() => location.href = '/')


        } catch (e) {
            showSnackbar(e.message)
        }

    }

    const initEventListeners = () => {
        $editButton.addEventListener("click", onEditHandler)
        $resignButton.addEventListener("click", onResignHandler)
    }

    const initMyPageEdit = () => {
        const id = localStorage.getItem("memberId")
        api.member.find(id)
            .then(data => {
                $email.innerHTML = data.email
                $name.value = data.name

                $editButton.setAttribute("data-id", data.id)
                $resignButton.setAttribute("data-id", data.id)
            })
    }

    this.init = () => {
        initMyPageEdit()
        initEventListeners()
    }
}

const mypageEdit = new MypageEdit()
mypageEdit.init()
