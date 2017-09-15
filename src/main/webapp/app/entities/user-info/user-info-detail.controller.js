(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('UserInfoDetailController', UserInfoDetailController);

    UserInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'UserInfo', 'User', 'NetworkMember', 'MetricHistory', 'ProgramHistory', 'Note', 'UserNotification', 'WellnessHistory', 'IncentiveHistory'];

    function UserInfoDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, UserInfo, User, NetworkMember, MetricHistory, ProgramHistory, Note, UserNotification, WellnessHistory, IncentiveHistory) {
        var vm = this;

        vm.userInfo = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('balancepositionApp:userInfoUpdate', function(event, result) {
            vm.userInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
