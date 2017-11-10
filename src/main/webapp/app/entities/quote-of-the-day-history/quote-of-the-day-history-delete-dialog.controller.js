(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('QuoteOfTheDayHistoryDeleteController',QuoteOfTheDayHistoryDeleteController);

    QuoteOfTheDayHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'QuoteOfTheDayHistory'];

    function QuoteOfTheDayHistoryDeleteController($uibModalInstance, entity, QuoteOfTheDayHistory) {
        var vm = this;

        vm.quoteOfTheDayHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            QuoteOfTheDayHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
