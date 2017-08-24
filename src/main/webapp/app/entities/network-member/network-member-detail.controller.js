(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('NetworkMemberDetailController', NetworkMemberDetailController);

    NetworkMemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NetworkMember', 'UserInfo'];

    function NetworkMemberDetailController($scope, $rootScope, $stateParams, previousState, entity, NetworkMember, UserInfo) {
        var vm = this;

        vm.networkMember = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:networkMemberUpdate', function(event, result) {
            vm.networkMember = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
