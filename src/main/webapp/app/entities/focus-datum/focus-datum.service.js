(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('FocusDatum', FocusDatum);

    FocusDatum.$inject = ['$resource', 'DateUtils'];

    function FocusDatum ($resource, DateUtils) {
        var resourceUrl =  'api/focus-data/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.timestamp = DateUtils.convertDateTimeFromServer(data.timestamp);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
