import { EVENT_TYPE, ERROR_MESSAGE } from '../../utils/constants.js'
import api from '../../api/index.js'
import showSnackbar from '../../lib/snackbar/index.js';

function MyInfo() {
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

const myPage = new MyInfo()
myPage.init()
