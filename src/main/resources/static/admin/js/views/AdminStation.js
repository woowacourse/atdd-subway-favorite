import { EVENT_TYPE, ERROR_MESSAGE, KEY_TYPE } from '../../utils/constants.js'
import { listItemTemplate } from '../../utils/templates.js'
import api from '../../api/index.js'

function AdminStation() {
  const $stationInput = document.querySelector('#station-name')
  const $stationList = document.querySelector('#station-list')
  const $stationAddButton = document.querySelector('#station-add-btn')

  const onAddStationHandler = event => {
    if (event.key && event.key !== KEY_TYPE.ENTER) {
      return
    }
    event.preventDefault()
    const $stationNameInput = document.querySelector('#station-name')
    const stationName = $stationNameInput.value
    if (!stationName) {
      alert(ERROR_MESSAGE.NOT_EMPTY)
      return
    }
    const newStation = {
      name: stationName
    }
    api.station
      .create(newStation)
      .then(() => {
        $stationNameInput.value = ''
        $stationList.insertAdjacentHTML('beforeend', listItemTemplate(stationName))
      })
      .catch(() => {
        alert('에러가 발생했습니다.')
      })
  }

  const onRemoveStationHandler = event => {
    const $target = event.target
    const isDeleteButton = $target.classList.contains('mdi-delete')
    if (isDeleteButton) {
      $target.closest('.list-item').remove()
    }
  }

  const initEventListeners = () => {
    $stationAddButton.addEventListener(EVENT_TYPE.CLICK, onAddStationHandler)
    $stationInput.addEventListener(EVENT_TYPE.KEY_PRESS, onAddStationHandler)
    $stationList.addEventListener(EVENT_TYPE.CLICK, onRemoveStationHandler)
  }

  const init = () => {
    initEventListeners()
  }

  return {
    init
  }
}

const adminStation = new AdminStation()
adminStation.init()
