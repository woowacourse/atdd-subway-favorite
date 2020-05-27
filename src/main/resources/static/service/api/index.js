const METHOD = {
  GET_WITH_AUTH() {
    return {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("jwt") || ""
      }
    };
  },
  PUT(data) {
    return {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("jwt") || ""
      },
      body: JSON.stringify({
        ...data
      })
    };
  },
  DELETE() {
    return {
      method: "DELETE",
      headers: {
        Authorization: localStorage.getItem("jwt") || ""
      }
    };
  },
  POST(data) {
    return {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("jwt") || ""
      },
      body: JSON.stringify({
        ...data
      })
    };
  }
};

const api = (() => {
  const requestWithRedirect = (uri, config) => fetch(uri, config).then(response => {
    if (response.redirected) {
      window.location.href = response.url;
    }
    return response;
  });
  const requestWithJsonData = (uri, config) =>
    fetch(uri, config).then(response => {
      if (!response.ok) {
        return;
      }
      return response.json();
    });

  const requestWithJsonDataAndRedirect = (uri, config) =>
    fetch(uri, config).then(response => {
      if (!response.ok) {
        return;
      }
      return response.json();
    });

  const member = {
    get(id) {
      return requestWithJsonDataAndRedirect(`/members/${id}`);
    },
    create(newMember) {
      return requestWithRedirect(`/members`, METHOD.POST(newMember));
    },
    update(id, updatedData) {
      return requestWithRedirect(`/members/${id}`, METHOD.PUT(updatedData));
    },
    delete(id) {
      return requestWithRedirect(`/members/${id}`, METHOD.DELETE());
    },
    login(loginInfo) {
      return requestWithJsonData(`/oauth/token`, METHOD.POST(loginInfo));
    }
  };

  const loginMember = {
    get() {
      return requestWithJsonData(`/me`, METHOD.GET_WITH_AUTH());
    },
    update(updatedInfo) {
      return requestWithRedirect(`/me`, METHOD.PUT(updatedInfo));
    },
    delete() {
      return requestWithRedirect(`/me`, METHOD.DELETE());
    }
  };

  const line = {
    getAll() {
      return requestWithRedirect(`/lines/detail`);
    },
    getAllDetail() {
      return requestWithJsonDataAndRedirect(`/lines/detail`);
    }
  };

  const path = {
    find(params) {
      return requestWithJsonDataAndRedirect(
        `/paths?source=${params.source}&target=${params.target}&type=${params.type}`
      );
    }
  };

  const favorite = {
    create(favoritePath) {
      return requestWithRedirect(`/me/favorites`, METHOD.POST(favoritePath));
    },
    get(sourceId, targetId) {
      return requestWithJsonDataAndRedirect(`/me/favorites/source/${sourceId}/target/${targetId}/existsPath`,
        METHOD.GET_WITH_AUTH());
    },
    getAll() {
      return requestWithJsonDataAndRedirect(`/me/favorites`, METHOD.GET_WITH_AUTH());
    },
    delete(sourceId, targetId) {
      return requestWithRedirect(`/me/favorites/source/${sourceId}/target/${targetId}`,
        METHOD.DELETE());
    }
  };

  return {
    member,
    loginMember,
    line,
    path,
    favorite
  };
})();

export default api;
