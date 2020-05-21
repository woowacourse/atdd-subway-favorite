import { EVENT_TYPE, ERROR_MESSAGE } from '../../utils/constants.js'
import api from '../../api/index.js'

function MyPage() {
  const onShow = async () => {
    await api.member.get().then(data =>  {
      document.querySelector("#user-email").innerText = data.email
      document.querySelector("#user-name").innerText = data.name
    })

  }

  this.init = () => {
    onShow()
  }
}

const myPage = new MyPage()
myPage.init()
