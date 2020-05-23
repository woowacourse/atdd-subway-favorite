const METHOD = {
    PUT(token, data) {
        return {
            method: 'PUT',
            headers: {
                'authorization': 'bearer ' + token,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                ...data
            })
        }
    },
    DELETE(token) {
        return {
            method: 'DELETE',
            headers: {
                'authorization': 'bearer ' + token,
            },
        }
    },
    POST(data) {
        return {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                ...data
            })
        }
    },
    TOKEN_POST(token, data) {
        return {
            method: 'POST',
            headers: {
                'authorization': 'bearer ' + token,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                ...data
            })
        }
    },
    GET(token) {
        return {
            method: 'GET',
            headers: {
                'authorization': 'bearer ' + token,
                'Content-Type': 'application/json'
            }
        }
    }
}

const api = (() => {
    const request = (uri, config) => fetch(uri, config)
    const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => data.json())

    const line = {
        getAll() {
            return request(`/lines/detail`)
        },
        getAllDetail() {
            return requestWithJsonData(`/lines/detail`)
        }
    }

    const path = {
        find(params) {
            return requestWithJsonData(`/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
        }
    }

    const member = {
        create(data) {
            return request('/members', METHOD.POST(data));
        },
        login(data) {
            return fetch('/oauth/token', METHOD.POST(data));
        },
        get(token) {
            return requestWithJsonData('/members/me', METHOD.GET(token));
        },
        update(token, data) {
            return request('/members/me', METHOD.PUT(token, data));
        },
        delete(token) {
            return request('/members/me', METHOD.DELETE(token));
        }
    }

    const favorite = {
        create(token, data) {
            return request('/favorite/me', METHOD.TOKEN_POST(token, data));
        },
        get(token) {
            return requestWithJsonData('/favorite/me', METHOD.GET(token));
        },
        delete(token, favoriteId) {
            return request('/favorite/me/' + favoriteId, METHOD.DELETE(token));
        }
    }

    return {
        line,
        path,
        member,
        favorite
    }
})()

export default api
