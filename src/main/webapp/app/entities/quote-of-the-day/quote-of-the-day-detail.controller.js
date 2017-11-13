(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('QuoteOfTheDayDetailController', QuoteOfTheDayDetailController);

    QuoteOfTheDayDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'QuoteOfTheDay', 'QuoteOfTheDayHistory'];

    function QuoteOfTheDayDetailController($scope, $rootScope, $stateParams, previousState, entity, QuoteOfTheDay, QuoteOfTheDayHistory) {
        var vm = this;

        vm.quoteOfTheDay = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:quoteOfTheDayUpdate', function(event, result) {
            vm.quoteOfTheDay = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();