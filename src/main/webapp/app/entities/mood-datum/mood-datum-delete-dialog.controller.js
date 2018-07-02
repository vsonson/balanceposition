(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('MoodDatumDeleteController',MoodDatumDeleteController);

    MoodDatumDeleteController.$inject = ['$uibModalInstance', 'entity', 'MoodDatum'];

    function MoodDatumDeleteController($uibModalInstance, entity, MoodDatum) {
        var vm = this;

        vm.moodDatum = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MoodDatum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
