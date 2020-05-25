import {
  optionTemplate,
  subwayLinesItemTemplate,
  subwayLinesTemplate
} from "../../utils/templates.js";
import tns from "../../lib/slider/tiny-slider.js";
import { EVENT_TYPE, ERROR_MESSAGE } from "../../utils/constants.js";
import Modal from "../../ui/Modal.js";
import api from "../../api/index.js";

function AdminEdge() {
  const $subwayLinesSlider = document.querySelector(".subway-lines-slider");
  const $createEdgeButton = document.querySelector("#create-edge-button");
  const $lineSelectOptions = document.querySelector("#line-select-options");
  const $subwayLineAddButton = document.querySelector("#subway-line-add-btn");
  const $nextStationSelectOptions = document.querySelector(
    "#next-station-select-options"
  );
  const createSubwayEdgeModal = new Modal();
  let subwayLines = [];

  const initSubwayLinesSlider = () => {
    tns({
      container: ".subway-lines-slider",
      loop: true,
      slideBy: "page",
      speed: 400,
      autoplayButtonOutput: false,
      mouseDrag: true,
      lazyload: true,
      controlsContainer: "#slider-controls",
      items: 1,
      edgePadding: 25
    });
  };

  const onDeleteStationHandler = event => {
    const $target = event.target;
    const isDeleteButton = $target.classList.contains("mdi-delete");
    if (!isDeleteButton) {
      return;
    }
    const lineId = $target.closest(".subway-line-item").dataset.id;
    const stationId = $target.closest(".list-item").dataset.id;
    api.line
      .deleteLineStation(lineId, stationId)
      .then(() => $target.closest(".list-item").remove())
      .catch(() => alert(ERROR_MESSAGE.COMMON));
  };

  const initSubwayLinesView = () => {
    api.line
      .getAllDetail()
      .then(data => {
        subwayLines = data.lineDetailResponse;
        if (subwayLines.length > 0) {
          $subwayLinesSlider.innerHTML = subwayLines
            .map(line => subwayLinesItemTemplate(line))
            .join("");
          initSubwayLinesSlider();
        }
      })
      .catch(error => {
        alert("데이터를 불러오는데 실패했습니다.");
      });
  };

  const onCreateEdgeHandler = event => {
    event.preventDefault();
    const lineId = $lineSelectOptions.value;
    const newEdge = {
      preStationId: document.querySelector("#previous-select-options").value,
      stationId: document.querySelector("#next-station-select-options").value,
      distance: document.querySelector("#distance").value,
      duration: document.querySelector("#duration").value
    };
    api.line
      .addLineStation(lineId, newEdge)
      .then(() => {
        createSubwayEdgeModal.toggle();
        initSubwayLinesView();
      })
      .catch(error => alert(ERROR_MESSAGE));
  };

  const initCreateEdgeForm = event => {
    event.preventDefault();
    initPreviousStationOptions();
    initNextStationOptions();
    initLineOptions(subwayLines);
  };

  const initPreviousStationOptions = () => {
    api.line.get($lineSelectOptions.value).then(data => {
      const stations = data.stations ? data.stations : [];
      if (stations.length > 0) {
        const $stationSelectOption = document.querySelector(
          "#previous-select-options"
        );
        $stationSelectOption.innerHTML = stations
          .map(station => optionTemplate(station))
          .join("");
      }
    });
  };

  const initNextStationOptions = () => {
    api.station
      .getAll()
      .then(stations => {
        $nextStationSelectOptions.innerHTML = stations
          .map(station => optionTemplate(station))
          .join("");
      })
      .catch(() => alert(ERROR_MESSAGE.COMMON));
  };

  const initLineOptions = subwayLines => {
    const subwayLineOptionTemplate = subwayLines
      .map(line => optionTemplate(line))
      .join("");
    const $lineSelectOption = document.querySelector("#line-select-options");
    $lineSelectOption.innerHTML = subwayLineOptionTemplate;
  };

  const initEventListeners = () => {
    $subwayLinesSlider.addEventListener(
      EVENT_TYPE.CLICK,
      onDeleteStationHandler
    );
    $createEdgeButton.addEventListener(EVENT_TYPE.CLICK, onCreateEdgeHandler);
    $subwayLineAddButton.addEventListener(EVENT_TYPE.CLICK, initCreateEdgeForm);
  };

  this.init = () => {
    initEventListeners();
    initSubwayLinesView();
  };
}

const adminEdge = new AdminEdge();
adminEdge.init();
