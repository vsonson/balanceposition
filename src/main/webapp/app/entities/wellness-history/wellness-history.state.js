(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wellness-history', {
            parent: 'entity',
            url: '/wellness-history?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WellnessHistories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wellness-history/wellness-histories.html',
                    controller: 'WellnessHistoryController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('wellness-history-detail', {
            parent: 'wellness-history',
            url: '/wellness-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WellnessHistory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wellness-history/wellness-history-detail.html',
                    controller: 'WellnessHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WellnessHistory', function($stateParams, WellnessHistory) {
                    return WellnessHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wellness-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wellness-history-detail.edit', {
            parent: 'wellness-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wellness-history/wellness-history-dialog.html',
                    controller: 'WellnessHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WellnessHistory', function(WellnessHistory) {
                            return WellnessHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wellness-history.new', {
            parent: 'wellness-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wellness-history/wellness-history-dialog.html',
                    controller: 'WellnessHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                wellnessscore: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wellness-history', null, { reload: 'wellness-history' });
                }, function() {
                    $state.go('wellness-history');
                });
            }]
        })
        .state('wellness-history.edit', {
            parent: 'wellness-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wellness-history/wellness-history-dialog.html',
                    controller: 'WellnessHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WellnessHistory', function(WellnessHistory) {
                            return WellnessHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wellness-history', null, { reload: 'wellness-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wellness-history.delete', {
            parent: 'wellness-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wellness-history/wellness-history-delete-dialog.html',
                    controller: 'WellnessHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WellnessHistory', function(WellnessHistory) {
                            return WellnessHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wellness-history', null, { reload: 'wellness-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
