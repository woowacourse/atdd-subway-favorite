import { optionTemplate, subwayLinesItemTemplate } from '../../utils/templates.js'
import tns from '../../lib/slider/tiny-slider.js'
import { EVENT_TYPE, ERROR_MESSAGE } from '../../utils/constants.js'
import Modal from '../../ui/Modal.js'
import api from '../../api/index.js'

function AdminEdge() {
  const $subwayLinesSlider = document.querySelector('.subway-lines-slider')
  const $createEdgeButton = document.querySelector('#create-edge-button')
  const $lineSelectOptions = document.querySelector('#line-select-options')
  const $subwayLineAddButton = document.querySelector('#subway-line-add-btn')
  const createSubwayEdgeModal = new Modal()
  let subwayLines = []

  const initSubwayLinesSlider = () => {
    tns({
      container: '.subway-lines-slider',
      loop: true,
      slideBy: 'page',
      speed: 400,
      autoplayButtonOutput: false,
      mouseDrag: true,
      lazyload: true,
      controlsContainer: '#slider-controls',
      items: 1,
      edgePadding: 25
    })
  }

  const onRemoveStationHandler = event => {
    const $target = event.target
    const isDeleteButton = $target.classList.contains('mdi-delete')
    if (isDeleteButton) {
      $target.closest('.list-item').remove()
    }
  }

  const initSubwayLinesView = () => {
    api.line
      .getAllDetail()
      .then(data => {
        subwayLines = data.lineDetailResponse
        if (subwayLines.length > 0) {
          $subwayLinesSlider.innerHTML = subwayLines.map(line => subwayLinesItemTemplate(line)).join('')
          initSubwayLinesSlider()
        }
      })
      .catch(error => {
        alert('데이터를 불러오는데 실패했습니다.')
      })
  }

  const initSubwayLineOptions = subwayLines => {
    const subwayLineOptionTemplate = subwayLines.map(line => optionTemplate(line)).join('')
    const $lineSelectOption = document.querySelector('#line-select-options')
    $lineSelectOption.innerHTML = subwayLineOptionTemplate
  }

  const initStationOptions = () => {
    api.line.get($lineSelectOptions.value).then(data => {
      const stations = data.stations ? data.stations : []
      if (stations.length > 0) {
        const $stationSelectOption = document.querySelector('#previous-select-options')
        $stationSelectOption.innerHTML = stations.map(station => optionTemplate(station)).join('')
      }
    })
  }

  const onCreateEdgeHandler = event => {
    event.preventDefault()
    const nextStationName = document.querySelector('#next-station-name').value
    api.station
      .create({ name: nextStationName })
      .then(station => {
        createEdge(station)
      })
      .catch(error => alert(ERROR_MESSAGE.COMMON))
  }

  const createEdge = station => {
    const lineId = $lineSelectOptions.value
    const newEdge = {
      preStationId: document.querySelector('#previous-select-options').value,
      stationId: station.id,
      distance: document.querySelector('#distance').value,
      duration: document.querySelector('#duration').value
    }
    api.line
      .addLineStation(lineId, newEdge)
      .then(() => {
        createSubwayEdgeModal.toggle()
        initSubwayLinesView()
      })
      .catch(error => alert(ERROR_MESSAGE))
  }

  const initCreateEdgeForm = event => {
    event.preventDefault()
    initSubwayLineOptions(subwayLines)
    initStationOptions()
  }

  const initEventListeners = () => {
    $subwayLinesSlider.addEventListener(EVENT_TYPE.CLICK, onRemoveStationHandler)
    $createEdgeButton.addEventListener(EVENT_TYPE.CLICK, onCreateEdgeHandler)
    $subwayLineAddButton.addEventListener(EVENT_TYPE.CLICK, initCreateEdgeForm)
  }

  this.init = () => {
    initSubwayLinesView()
    initEventListeners()
  }
}

const adminEdge = new AdminEdge()
adminEdge.init()
