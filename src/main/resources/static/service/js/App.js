import {initNavigation} from '../utils/templates.js'

function SubwayApp() {
    this.init = () => {
        initNavigation()
    }
}

const subwayApp = new SubwayApp()
subwayApp.init()
