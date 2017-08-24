(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('NetworkMember', NetworkMember);

    NetworkMember.$inject = ['$resource'];

    function NetworkMember ($resource) {
        var resourceUrl =  'api/network-members/:id';

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
