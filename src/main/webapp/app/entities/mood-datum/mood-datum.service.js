(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('MoodDatum', MoodDatum);

    MoodDatum.$inject = ['$resource', 'DateUtils'];

    function MoodDatum ($resource, DateUtils) {
        var resourceUrl =  'api/mood-data/:id';

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
