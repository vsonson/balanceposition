(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('Alert', Alert);

    Alert.$inject = ['$resource'];

    function Alert ($resource) {
        var resourceUrl =  'api/alerts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
