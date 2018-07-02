(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('MoodDatumDetailController', MoodDatumDetailController);

    MoodDatumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MoodDatum', 'User'];

    function MoodDatumDetailController($scope, $rootScope, $stateParams, previousState, entity, MoodDatum, User) {
        var vm = this;

        vm.moodDatum = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:moodDatumUpdate', function(event, result) {
            vm.moodDatum = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
