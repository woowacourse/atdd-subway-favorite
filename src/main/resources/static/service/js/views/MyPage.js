import api from '../../api/index.js'

function MyPage() {
    const $emailInput = document.querySelector('#email')
    const $nameInput = document.querySelector('#name')

    this.init = async () => {
        try {
            const myInfo = await api.me.retrieve();
            $emailInput.innerHTML = myInfo.email
            $nameInput.innerHTML = myInfo.name
        } catch (e) {
            Snackbar.show({text: e, pos: 'bottom-center', showAction: false, duration: 2000})
        }
    }
}

const myPage = new MyPage()
myPage.init()
