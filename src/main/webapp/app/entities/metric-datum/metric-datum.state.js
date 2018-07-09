(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('metric-datum', {
            parent: 'entity',
            url: '/metric-datum',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MetricData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metric-datum/metric-data.html',
                    controller: 'MetricDatumController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('metric-datum-detail', {
            parent: 'metric-datum',
            url: '/metric-datum/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MetricDatum'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metric-datum/metric-datum-detail.html',
                    controller: 'MetricDatumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MetricDatum', function($stateParams, MetricDatum) {
                    return MetricDatum.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'metric-datum',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('metric-datum-detail.edit', {
            parent: 'metric-datum-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric-datum/metric-datum-dialog.html',
                    controller: 'MetricDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MetricDatum', function(MetricDatum) {
                            return MetricDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('metric-datum.new', {
            parent: 'metric-datum',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric-datum/metric-datum-dialog.html',
                    controller: 'MetricDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                metricValue: null,
                                timestamp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('metric-datum', null, { reload: 'metric-datum' });
                }, function() {
                    $state.go('metric-datum');
                });
            }]
        })
        .state('metric-datum.edit', {
            parent: 'metric-datum',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric-datum/metric-datum-dialog.html',
                    controller: 'MetricDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MetricDatum', function(MetricDatum) {
                            return MetricDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('metric-datum', null, { reload: 'metric-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('metric-datum.delete', {
            parent: 'metric-datum',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metric-datum/metric-datum-delete-dialog.html',
                    controller: 'MetricDatumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MetricDatum', function(MetricDatum) {
                            return MetricDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('metric-datum', null, { reload: 'metric-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
