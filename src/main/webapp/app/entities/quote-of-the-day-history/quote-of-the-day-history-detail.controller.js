(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('QuoteOfTheDayHistoryDetailController', QuoteOfTheDayHistoryDetailController);

    QuoteOfTheDayHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'QuoteOfTheDayHistory', 'UserInfo', 'QuoteOfTheDay'];

    function QuoteOfTheDayHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, QuoteOfTheDayHistory, UserInfo, QuoteOfTheDay) {
        var vm = this;

        vm.quoteOfTheDayHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:quoteOfTheDayHistoryUpdate', function(event, result) {
            vm.quoteOfTheDayHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
