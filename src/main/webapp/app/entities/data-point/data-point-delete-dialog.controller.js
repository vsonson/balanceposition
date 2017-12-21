(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('DataPointDeleteController',DataPointDeleteController);

    DataPointDeleteController.$inject = ['$uibModalInstance', 'entity', 'DataPoint'];

    function DataPointDeleteController($uibModalInstance, entity, DataPoint) {
        var vm = this;

        vm.dataPoint = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DataPoint.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
