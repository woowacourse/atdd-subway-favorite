import { EVENT_TYPE, ERROR_MESSAGE } from '../../utils/constants.js'
import api from '../../api/index.js'
import showSnackbar from '../../lib/snackbar/index.js';

function MyPage() {
  const onShow = async () => {
    try {
      await api.loginMember.get().then(
        data =>  {
          document.querySelector("#user-email").innerText = data.email
          document.querySelector("#user-name").innerText = data.name
        }
      )
    } catch (e) {
      showSnackbar(ERROR_MESSAGE.COMMON)
    }
  }

  this.init = () => {
    onShow()
  }
}

const myPage = new MyPage()
myPage.init()
