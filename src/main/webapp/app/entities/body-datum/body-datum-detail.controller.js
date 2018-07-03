(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('BodyDatumDetailController', BodyDatumDetailController);

    BodyDatumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BodyDatum', 'User'];

    function BodyDatumDetailController($scope, $rootScope, $stateParams, previousState, entity, BodyDatum, User) {
        var vm = this;

        vm.bodyDatum = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:bodyDatumUpdate', function(event, result) {
            vm.bodyDatum = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
