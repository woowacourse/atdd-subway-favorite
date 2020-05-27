const METHOD = {
    PUT() {
        return {
            method: 'PUT'
        }
    },
    DELETE() {
        return {
            method: 'DELETE'
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
            return request(`/members/signup`, METHOD.POST(data));
        },
        login(data) {
            return request(`/oauth/token`, METHOD.POST(data));
        },
        find(token) {
            return fetch(`/members`, {
                headers: {
                    'Authorization': token,
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {
                    alert('로그인을 다시 해주세요 :)');
                    window.location.href = "/";
                }
                return response.json();
            })
        },
        update(token, data) {
            return fetch(`/members`, {
                method: 'PUT',
                headers: {
                    'Authorization': token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
        },
        signOut(token) {
            return fetch(`/members`, {
                method: 'DELETE',
                headers: {
                    'Authorization': token
                }
            })
        }
    };

    const favorite = {
        create(token, data) {
            return fetch(`/members/favorites`, {
                method: 'POST',
                headers: {
                    'Authorization': token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    ...data
                })
            })
        },
        find(token) {
            return fetch(`/members/favorites`, {
                headers: {
                    'Authorization': token,
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {
                    alert('로그인을 다시 해주세요 :)');
                    window.location.href = "/";
                }
                return response.json();
            })
        },
        delete(token, data) {
            return fetch(`/members/favorites`, {
                method: 'DELETE',
                headers: {
                    'Authorization': token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    ...data
                })
            })
        }
    };

    return {
        line,
        path,
        member,
        favorite
    }
})()

export default api
