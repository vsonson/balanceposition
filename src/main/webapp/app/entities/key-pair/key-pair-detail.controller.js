(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('KeyPairDetailController', KeyPairDetailController);

    KeyPairDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'KeyPair', 'TrackMetricQuestion'];

    function KeyPairDetailController($scope, $rootScope, $stateParams, previousState, entity, KeyPair, TrackMetricQuestion) {
        var vm = this;

        vm.keyPair = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:keyPairUpdate', function(event, result) {
            vm.keyPair = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
