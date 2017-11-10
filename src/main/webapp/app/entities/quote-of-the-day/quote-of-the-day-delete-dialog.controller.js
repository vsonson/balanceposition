(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('QuoteOfTheDayDeleteController',QuoteOfTheDayDeleteController);

    QuoteOfTheDayDeleteController.$inject = ['$uibModalInstance', 'entity', 'QuoteOfTheDay'];

    function QuoteOfTheDayDeleteController($uibModalInstance, entity, QuoteOfTheDay) {
        var vm = this;

        vm.quoteOfTheDay = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            QuoteOfTheDay.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
