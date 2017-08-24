(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('NotifcationTrigger', NotifcationTrigger);

    NotifcationTrigger.$inject = ['$resource'];

    function NotifcationTrigger ($resource) {
        var resourceUrl =  'api/notifcation-triggers/:id';

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
