import Snackbar from "./snackbar.js";

export default function showSnackbar(message) {
    Snackbar.show({
        text: message,
        pos: "bottom-center",
        showAction: true,
        duration: 2000
    });
}