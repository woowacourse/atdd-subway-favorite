import user from '../../api/user.js'

function MyPage() {
  const $email = document.querySelector('#email')
  const $name = document.querySelector('#name')

  this.init = async () => {
    !user.isLoggedIn() && location.replace('/login')

    try {
      const { email, name } = await user.getInfo()
      $email.innerHTML = email
      $name.innerHTML = name
    }
    catch (error) {
      Snackbar.show({
        text: error.message,
        pos: 'bottom-center',
        showAction: false,
        duration: 2000
      })
    }
  }
}

const myPage = new MyPage()
myPage.init()
