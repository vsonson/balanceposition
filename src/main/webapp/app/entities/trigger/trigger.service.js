(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('Trigger', Trigger);

    Trigger.$inject = ['$resource'];

    function Trigger ($resource) {
        var resourceUrl =  'api/triggers/:id';

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
