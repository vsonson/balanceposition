(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('UserInfo', UserInfo);

    UserInfo.$inject = ['$resource', 'DateUtils'];

    function UserInfo ($resource, DateUtils) {
        var resourceUrl =  'api/user-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateOfBirth = DateUtils.convertDateTimeFromServer(data.dateOfBirth);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
