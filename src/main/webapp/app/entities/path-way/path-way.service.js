(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('PathWay', PathWay);

    PathWay.$inject = ['$resource'];

    function PathWay ($resource) {
        var resourceUrl =  'api/path-ways/:id';

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
