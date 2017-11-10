(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('QuoteOfTheDayDialogController', QuoteOfTheDayDialogController);

    QuoteOfTheDayDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'QuoteOfTheDay'];

    function QuoteOfTheDayDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, QuoteOfTheDay) {
        var vm = this;

        vm.quoteOfTheDay = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.quoteOfTheDay.id !== null) {
                QuoteOfTheDay.update(vm.quoteOfTheDay, onSaveSuccess, onSaveError);
            } else {
                QuoteOfTheDay.save(vm.quoteOfTheDay, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:quoteOfTheDayUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
