import { EVENT_TYPE } from "../utils/constants.js";
import Modal from "./Modal.js";

export default function CustomModal() {
  this.toggle = new Modal().toggle
  console.log(this.toggle)
}
