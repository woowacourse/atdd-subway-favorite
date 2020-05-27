import user from '../../api/user.js'

function Logout() {
  this.init = () => {
    user.logout()
    location.replace('/service')
  }
}

const logout = new Logout()
logout.init()
